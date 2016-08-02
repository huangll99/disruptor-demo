package com.hll.disruptor.diamond;

import com.hll.disruptor.commons.OrderEventFactory;
import com.hll.disruptor.model.OrderEvent;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 菱形消费结构
 * Created by hll on 2016/8/1.
 */
public class DiamondDemo {
  public static void main(String[] args) throws InterruptedException {
    //消费线程池
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    EventFactory<OrderEvent> eventFactory = new OrderEventFactory();
    Disruptor<OrderEvent> disruptor = new Disruptor<>(eventFactory, 1024 * 1024, executorService, ProducerType.SINGLE, new BusySpinWaitStrategy());

    disruptor.handleEventsWith(new AccessNameHandler(), new AccessPriceHandler()).then(new FinalEventHandler());

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
