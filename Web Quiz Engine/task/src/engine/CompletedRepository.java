package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletedRepository extends PagingAndSortingRepository<Completed, Long> {
    @Query("SELECT c FROM Completed c where c.user.email=:email order by c.completedAt desc")
    Page<Completed> findByEmail(String email, Pageable pageable);
}
