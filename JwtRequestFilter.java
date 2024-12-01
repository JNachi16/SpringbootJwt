@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String jwtToken = extractJwtToken(request);  // Extract token from request
        if (jwtToken != null && jwtUtils.validateToken(jwtToken)) {
            String username = jwtUtils.extractUsername(jwtToken);  // Extract username from token

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);  // Load user details
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());  // Create authentication token
            SecurityContextHolder.getContext().setAuthentication(authentication);  // Set the authentication in the context
        }
        
        filterChain.doFilter(request, response);  // Continue the request-response chain
    }

    // Extract JWT token from the "Authorization" header
    private String extractJwtToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;  // No token found
    }
}
