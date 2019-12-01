package repository;

import db.DbConnection;
import java.sql.SQLException;

import model.User;

/**
 *
 * @author vahid
 */
public class UserRepository extends DbConnection {

    public User findUser() {
        User user = null;
        String query = "select * from user where id=1 and status=1";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }

        return user;

    }

    public void deleteUser() {
        String query = "update user set status=0 and id=1";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();

        }
    }

    public void registerUser(User user) {
        String query = "update user set name=? , surname=? , username=?, password=? , status=1 where id=1";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.execute();

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
    }
}
