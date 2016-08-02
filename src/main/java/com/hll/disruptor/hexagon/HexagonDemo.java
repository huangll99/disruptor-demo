package com.hll.disruptor.hexagon;

import com.hll.disruptor.commons.OrderEventFactory;
import com.hll.disruptor.diamond.AccessNameHandler;
import com.hll.disruptor.diamond.AccessPriceHandler;
import com.hll.disruptor.diamond.FinalEventHandler;
import com.hll.disruptor.diamond.MyProducer;
import com.hll.disruptor.model.OrderEvent;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hll on 2016/8/1.
 */
public class HexagonDemo {

  public static void main(String[] args) throws InterruptedException {
    //消费线程池
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    EventFactory<OrderEvent> eventFactory = new OrderEventFactory();
    Disruptor<OrderEvent> disruptor = new Disruptor<>(eventFactory, 1024 * 1024, executorService, ProducerType.SINGLE, new BusySpinWaitStrategy());

    AccessNameHandler accessNameHandler = new AccessNameHandler();
    AccessPriceHandler accessPriceHandler = new AccessPriceHandler();
    disruptor.handleEventsWith(accessNameHandler,accessPriceHandler);

    LogEventHandlerOne logEventHandlerOne = new LogEventHandlerOne();
    LogEventHandlerTwo logEventHandlerTwo = new LogEventHandlerTwo();
    disruptor.after(accessNameHandler).then(logEventHandlerOne);
    disruptor.after(accessPriceHandler).then(logEventHandlerTwo);
    disruptor.after(logEventHandlerOne,logEventHandlerTwo).then(new FinalEventHandler());

    disruptor.start();

    MyProducer producer = new MyProducer();

    for (long i = 0; i < 100; i++) {
      producer.onData(disruptor.getRingBuffer(), i, "tom-" + i, 20.8 + i);
    }

    Thread.sleep(5000);

    disruptor.shutdown();
    executorService.shutdown();
  }
}
