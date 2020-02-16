package ru.milovtim.bonds.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.Test;

class TinkofInvestServiceTest {

    @Test
    void getPortfolioByAccount() {
        Observable<Integer> o1 = Observable.fromArray(1, 2, 3);
        Observable<Integer> o2 = Observable.fromArray(4, 5, 6);
        TestObserver<Integer> merge = Observable.merge(o1, o2).test();
        merge.assertValues(1, 2, 3, 4, 5, 6);
    }


    @Test
    void some() {
        TestObserver<String> testSubscriber = Observable.merge(
            Observable.fromArray("Hello", "World"),
            Observable.fromArray("I love", "RxJava")
        ).test();
        testSubscriber.assertValues("Hello", "World", "I love", "RxJava");
        testSubscriber.dispose();
    }

    @Test
    void hello() throws InterruptedException {
        Disposable sb = Observable
            .interval(1500, TimeUnit.MILLISECONDS)
            .subscribe(x -> System.out.println("Emitted " + x));
        TimeUnit.SECONDS.sleep(5);
        sb.dispose();
    }
}