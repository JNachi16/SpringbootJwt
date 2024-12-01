@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    // Get all flights
    public List<FlightDTO> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        return flights.stream()
            .map(flight -> new FlightDTO(flight.getId(), flight.getFlightNumber(), flight.getDeparture(), flight.getDestination()))
            .collect(Collectors.toList());
    }

    // Get a flight by ID
    public FlightDTO getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));
        return new FlightDTO(flight.getId(), flight.getFlightNumber(), flight.getDeparture(), flight.getDestination());
    }

    // Add a new flight
    public FlightDTO addFlight(FlightDTO flightDTO) {
        Flight flight = new Flight();
        flight.setFlightNumber(flightDTO.getFlightNumber());
        flight.setDeparture(flightDTO.getDeparture());
        flight.setDestination(flightDTO.getDestination());

        Flight savedFlight = flightRepository.save(flight);
        return new FlightDTO(savedFlight.getId(), savedFlight.getFlightNumber(), savedFlight.getDeparture(), savedFlight.getDestination());
    }

    // Update an existing flight
    public FlightDTO updateFlight(Long id, FlightDTO flightDTO) {
        Flight existingFlight = flightRepository.findById(id)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));

        existingFlight.setFlightNumber(flightDTO.getFlightNumber());
        existingFlight.setDeparture(flightDTO.getDeparture());
        existingFlight.setDestination(flightDTO.getDestination());

        Flight updatedFlight = flightRepository.save(existingFlight);
        return new FlightDTO(updatedFlight.getId(), updatedFlight.getFlightNumber(), updatedFlight.getDeparture(), updatedFlight.getDestination());
    }

    // Delete a flight
    public void deleteFlight(Long id) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));

        flightRepository.delete(flight);
    }
}
