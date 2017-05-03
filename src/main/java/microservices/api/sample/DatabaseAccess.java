package microservices.api.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import microservices.api.sample.model.Airline;
import microservices.api.sample.model.Booking;

public class DatabaseAccess {
	
	public static void main (String[] args) {
		getAllAirlines();
	}

	
	private static String DATABASE_CORE_ADDRESS;
	private static String AIRLINES_DATABASE;
	private static String BOOKINGS_DATABASE;
	private static final String ALL_QUERY = "/_all_docs";
	private static final ObjectMapper mapper = new ObjectMapper();

	
	static {
		Properties props = new Properties();
		try {
			props.load(DatabaseAccess.class.getClassLoader().getResourceAsStream("config.properties"));
			DATABASE_CORE_ADDRESS = props.getProperty("database");
			AIRLINES_DATABASE = DATABASE_CORE_ADDRESS + "airlines";
			BOOKINGS_DATABASE = DATABASE_CORE_ADDRESS + "bookings";
			System.out.println("loaded cconfig. Database: " + DATABASE_CORE_ADDRESS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Collection<Airline> getAllAirlines() {
		JsonNode response = HttpHelper.connect(AIRLINES_DATABASE + ALL_QUERY, "GET", null);
		if (response == null) {
			return null;
		}
		int size = response.get("total_rows").asInt();
		System.out.println("Number of airlines: " + size);
		JsonNode airlines = response.get("rows");
		List<Airline> allAirlines = new ArrayList<Airline>(size);
		for (int i = 0; i < size; i++) {
			try {
				JsonNode airlineJson = HttpHelper.connect(AIRLINES_DATABASE + "/" + airlines.get(i).get("id").asText(), "GET", null);
				Airline airline = mapper.treeToValue(airlineJson, Airline.class);
				System.out.println("Airline[" + i + "] " + mapper.writeValueAsString(airline));
				allAirlines.add(airline);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return allAirlines;
	}
	
	public static Collection<Booking> getAllBookings() {
		JsonNode response = HttpHelper.connect(BOOKINGS_DATABASE + ALL_QUERY, "GET", null);
		if (response == null) {
			return null;
		}
		int size = response.get("total_rows").asInt();
		System.out.println("Number of bookings: " + size);
		JsonNode bookings = response.get("rows");
		List<Booking> allBookings = new ArrayList<Booking>(size);
		for (int i = 0; i < size; i++) {
			try {
				JsonNode bookingJson = HttpHelper.connect(BOOKINGS_DATABASE + "/" + bookings.get(i).get("id").asText(), "GET", null);
				Booking booking = mapper.treeToValue(bookingJson,Booking.class);
				System.out.println("Booking[" + i + "] " + booking);
				//todo: add id
				allBookings.add(booking);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return allBookings;
	}
	
	public static String addBooking(Booking booking) {
		try {
			JsonNode response = HttpHelper.connect(BOOKINGS_DATABASE, "POST", mapper.writeValueAsString(booking));
			return response.get("id").asText();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Booking getBooking(String id) {
		JsonNode response = HttpHelper.connect(BOOKINGS_DATABASE + "/" + id, "GET", null);
		try {
			return mapper.treeToValue(response,Booking.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void updateBooking(String id, Booking booking) {
		//We need to get the current _rev from the DB first
		JsonNode response = HttpHelper.connect(BOOKINGS_DATABASE + "/" + id, "GET", null);
		String _rev = response.get("_rev").asText();
		
		//Now let's build the new booking
		JsonNode updatedBooking = mapper.valueToTree(booking);
		((ObjectNode)updatedBooking).put("_id", id);
		((ObjectNode)updatedBooking).put("_rev", _rev);

		//Update database
		try {
			HttpHelper.connect(BOOKINGS_DATABASE + "/" + id, "PUT", mapper.writeValueAsString(updatedBooking));
			//TODO: handle update conflict case
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public static void removeBooking(String id) {
		//We need to get the current _rev from the DB first
		JsonNode response = HttpHelper.connect(BOOKINGS_DATABASE + "/" + id, "GET", null);
		String rev = response.get("_rev").asText();
		
		//Issue with delete command, with rev as a query param
		HttpHelper.connect(BOOKINGS_DATABASE + "/" + id + "?rev=" + rev, "DELETE", null);
		//TODO: handle update conflict case
	}
	
}
