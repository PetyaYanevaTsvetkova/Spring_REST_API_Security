package coherentsolutions.airapp.repository;

import coherentsolutions.airapp.model.entity.Ticket;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

    @Query(nativeQuery = true, value = "select t.user_id from Tickets as t group by t.user_id having count(t.id) >= " +
            "ALL(select count(t.id) from Tickets t group by t.user_id)")
    Long mostActiveUserId();

    @Query(nativeQuery = true, value = "select t.plane_id from Tickets as t group by t.plane_id having count(t.id) >=" +
            " ALL(select count(t.id) from Tickets t group by t.plane_id)")
    Long mostBoughtTicketsPlaneId();

}
