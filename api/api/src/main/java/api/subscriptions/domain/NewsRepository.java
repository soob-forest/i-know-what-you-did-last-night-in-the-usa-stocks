package api.subscriptions.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

  @Query("select n from News n where n.date = :date and n.stock in :stocks")
  List<News> findAllByDateAndStockIn(LocalDate date, List<Stock> stocks);

  @Query("select distinct n from News n left join fetch n.links where n.date = :date and n.stock in :stocks")
  List<News> findAllWithLinksByDateAndStockIn(LocalDate date, List<Stock> stocks);
}
