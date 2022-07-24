package tacos.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import tacos.TacoOrder;

public interface OrderRepository extends CrudRepository<TacoOrder, Long>{
	
	List<TacoOrder> findByDeliveryZip(String deliveryZip);
	
}