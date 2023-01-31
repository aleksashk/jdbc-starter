package com.philimonov.jdbc.starter;

import com.philimonov.jdbc.starter.util.ConnectionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BlobRunner {
    public static void main(String[] args) throws SQLException, IOException {
        //blob / bytea(postgres)
        //binary large object
        //clob / TEXT(postgres)
        //character large object
        saveImage();
    }

    private static void saveImage() throws SQLException, IOException {
        String sql = """
                update aircraft set image = ? where id = 1;
                """;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            Blob blob = connection.createBlob();
            blob.setBytes(1, Files.readAllBytes(Path.of("resources", "Emirates-B777.jpg")));
            preparedStatement.setBlob(1, blob);
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }
}
