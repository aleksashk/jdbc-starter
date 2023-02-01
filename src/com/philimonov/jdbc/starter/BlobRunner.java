package com.philimonov.jdbc.starter;

import com.philimonov.jdbc.starter.util.ConnectionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobRunner {
    public static void main(String[] args) throws SQLException, IOException {
        //blob / bytea(postgres)
        //binary large object
        //clob / TEXT(postgres)
        //character large object
        saveImage();
        getImage();
    }

    private static void getImage() throws SQLException, IOException{
        String sql = """
                select image from aircraft where id = ?
                """;
        try(Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                byte[] image = resultSet.getBytes("image");
                Files.write(Path.of("resources", "boing777.jpg"), image, StandardOpenOption.CREATE);
            }
        }
    }

    private static void saveImage() throws SQLException, IOException {
        String sql = """
                update aircraft set image = ? where id = 1;
                """;
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setBytes(1, Files.readAllBytes(Path.of("resources", "Emirates-B777.jpg")));
            preparedStatement.executeUpdate();
        }
    }
}
