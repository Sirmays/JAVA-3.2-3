package Server;

import java.sql.*;
import java.util.Optional;

public class UsersRepository {

    public Optional<Entry> findByLoginAndPassword(String login, String password) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE login = ? AND password = ?"
            );
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return Optional.of(
                        new Entry(
                                rs.getString("name"),
                                rs.getString("login"),
                                rs.getString("password")
                        )
                );
            }
        } catch (SQLException ex) {
            throw new RuntimeException("SWW", ex);
        }

        return Optional.empty();
    }
}