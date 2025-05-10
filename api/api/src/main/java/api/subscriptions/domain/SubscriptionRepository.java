package api.subscriptions.domain;

import java.sql.Time;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  Page<Subscription> findAllByPushAt(Time pushAt, Pageable pageable);
}
