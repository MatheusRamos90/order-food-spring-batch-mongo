package br.com.matheushramos.batch.job.reader;

import br.com.matheushramos.batch.enums.StatusEnum;
import br.com.matheushramos.batch.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class OrderFoodReader extends MongoItemReader<Order> {

    public static final Integer PAGE_SIZE = 5;

    MongoTemplate mongoTemplate;

    private boolean lastPage;

    public OrderFoodReader(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        setTemplate(mongoTemplate);
        setPageSize(PAGE_SIZE);
        setSaveState(false);
        setTargetType(Order.class);
    }

    @Override
    public Iterator<Order> doPageRead() {
        if (this.lastPage) {
            log.info("Last page executed. No more records to read.");
            return null;
        }

        final var pageable = PageRequest.of(this.page, this.pageSize);
        final var skipValue = (long) pageable.getPageNumber() * (long) pageable.getPageSize();

        log.info("Reading orders... Page: {}", pageable.getPageNumber());

        Query q = new Query();
        q.addCriteria(Criteria.where("status").is(StatusEnum.SUCCESS));
        q.skip(skipValue);
        q.limit(pageable.getPageSize());

        List<Order> orders = this.mongoTemplate.find(q, Order.class);

        log.info("Read orders. Total: {}, Page: {}", orders.size(), pageable.getPageNumber());

        if (orders.size() < PAGE_SIZE.doubleValue()) {
            this.lastPage = true;
        }

        return orders.iterator();
    }

    @Override
    public void afterPropertiesSet() {
        //
    }
}
