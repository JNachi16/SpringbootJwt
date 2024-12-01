@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // Register a new user
    public void registerUser(UserDTO userDTO) {
        // Check if user already exists
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        // Create and save the user
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(hashedPassword);
        user.setRoles(userDTO.getRoles()); // Set roles (can be default or passed in request)

        userRepository.save(user);
    }

    // Authenticate user and generate JWT token
    public String authenticateUser(AuthenticationRequest authRequest) {
        // Fetch the user from the database
        User user = userRepository.findByUsername(authRequest.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Validate the password
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Generate JWT token
        return jwtUtils.generateToken(user.getUsername(), user.getRoles());
    }
}
