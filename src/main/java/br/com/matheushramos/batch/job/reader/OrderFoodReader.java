package br.com.matheushramos.batch.job.reader;

import br.com.matheushramos.batch.enums.StatusEnum;
import br.com.matheushramos.batch.model.Order;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Setter
public class OrderFoodReader extends MongoItemReader<Order> {

    MongoTemplate mongoTemplate;

    public OrderFoodReader(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        setTemplate(mongoTemplate);
        setPageSize(100);
        setSaveState(false);
        setTargetType(Order.class);
    }

    @Override
    public Iterator<Order> doPageRead() {
        final var pageable = PageRequest.of(this.page, this.pageSize);
        final var skipValue = (long) pageable.getPageNumber() * (long) pageable.getPageSize();

        log.info("Reading orders... Page: {}", pageable.getPageNumber());

        Query q = new Query();
        q.addCriteria(Criteria.where("status").is(StatusEnum.SUCCESS));
        q.skip(skipValue);
        q.limit(pageable.getPageSize());

        List<Order> orders = this.mongoTemplate.find(q, Order.class);

        log.info("Read orders. Total: {}", orders.size());

        return orders.iterator();
    }

    @Override
    public void afterPropertiesSet() {
        //
    }
}
