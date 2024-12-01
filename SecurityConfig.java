@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/users/register", "/api/users/login").permitAll()  // Allow public access to registration and login
                .antMatchers("/api/flights/**").hasRole("ADMIN")  // Admin role for flight CRUD operations
                .antMatchers("/api/bookings/**").hasRole("USER")  // User role for booking operations
                .anyRequest().authenticated()  // Any other request needs to be authenticated
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // Custom entry point for unauthorized access
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);  // Stateless authentication (no session storage)
        
        // Add the JWT filter to the security filter chain
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)  // Use custom user details service for authentication
            .passwordEncoder(passwordEncoder());  // Use password encoder
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Use BCryptPasswordEncoder for password hashing
    }
}
