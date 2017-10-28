package me.breidenbach.util.examples;

import me.breidenbach.util.Failure;
import me.breidenbach.util.Success;
import me.breidenbach.util.Try;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Kevin E. Breidenbach
 * Date: 10/15/17
 */
class TryExample {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    private TryExample() {
        MockitoAnnotations.initMocks(this);
        try {
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.execute()).thenThrow(new SQLException("Thrown just for this example!"));
        } catch (final SQLException ignored) {
        }
    }

    /* Why not use method references?
     * Because the way Java handles method references is that it if the object being run on is null it will throw
     * a NPE before the lambda. What we really want to happen is the NPE thrown within the lambda so it can be caught
     * in the Try. However, if you know your object can't be null then method
     */
    private Try<Connection> getConnection() {
        return Try.tryRun(() -> dataSource.getConnection());
    }

    private Try<PreparedStatement> prepareStatement(final Connection connection, final String sql) {
        return Try.tryRun(() -> connection.prepareStatement(sql));
    }

    private Try<String> successString() {
        return new Success<>("Yup!");
    }

    private Try<String> failureString() {
        return new Failure<>(new Exception("Nope!"));
    }

    public static void main(final String[] args) {
        final String sql = "SELECT * FROM MY_TABLE";
        final TryExample example = new TryExample();

        // Don't care about failure example
        example.getConnection().onSuccess(
                connection -> example.prepareStatement(connection, sql).onSuccess(
                        statement -> Try.tryRun(statement::execute)));

        // Do something about failure
        example.getConnection().
                then(TryExample::logError, connection -> example.prepareStatement(connection, sql).
                        then(TryExample::logError,
                                statement -> Try.tryRun(statement::execute).onFailure(TryExample::logError)));

        if (!example.successString().fold(error -> false, string -> true)) throw new RuntimeException("Should be success");
        if (example.failureString().fold(error -> false, string -> true)) throw new RuntimeException("Should have failed");
    }

    private static void logError(final Throwable throwable) {
        System.out.println(throwable.getMessage());
    }
}
