@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    // Getters and Setters
}
