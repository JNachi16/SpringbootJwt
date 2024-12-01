@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByOriginAndDestination(String origin, String destination);

    List<Flight> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);
}
