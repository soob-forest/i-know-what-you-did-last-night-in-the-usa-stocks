package api.subscriptions.domain;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, Long> {

  Optional<Stock> findByTicker(String ticker);
}
