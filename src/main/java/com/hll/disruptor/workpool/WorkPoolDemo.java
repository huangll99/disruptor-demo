package com.hll.disruptor.workpool;

import com.hll.disruptor.commons.OrderEventFactory;
import com.hll.disruptor.diamond.MyProducer;
import com.hll.disruptor.model.OrderEvent;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hll on 2016/8/1.
 */
public class WorkPoolDemo {
  public static void main(String[] args) {
    //消费线程池
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    EventFactory<OrderEvent> eventFactory = new OrderEventFactory();
    Disruptor<OrderEvent> disruptor = new Disruptor<>(eventFactory, 1024 * 1024, executorService, ProducerType.MULTI, new YieldingWaitStrategy());

    disruptor.handleEventsWithWorkerPool(new MyWorkHandler("ID-1"), new MyWorkHandler("ID-2"), new MyWorkHandler("ID-3"));

    disruptor.start();

    MyProducer producer = new MyProducer();

    for (int i = 0; i < 3; i++) {
      Thread t = new Thread() {
        @Override
        public void run() {
          for (long l = 0; l < 100; l++) {
            producer.onData(disruptor.getRingBuffer(), l, "username-" + l, 1.0 + l);
          }
        }
      };
      t.start();
    }
  }
}
