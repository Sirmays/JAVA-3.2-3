package Server;

public class AuthenticationService {
    private final UsersRepository repository;

    public AuthenticationService(UsersRepository repository) {
        this.repository = repository;
    }


    public Entry findUser(String login, String password) {
        return repository.findByLoginAndPassword(login, password)
                .orElseThrow(
                        () -> new RuntimeException("No user found with login " + login)
                );
    }
    public UsersRepository getRepository() {
        return repository;
    }
}