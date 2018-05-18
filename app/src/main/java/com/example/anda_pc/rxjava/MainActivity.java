package com.example.anda_pc.rxjava;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        final Observable<String> operationObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(longRunningOperation());
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());*/
        Button startRxOperationButton = findViewById(R.id.start_btn);


/*        startRxOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                 subscription = operationObservable.subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String o) {
                        v.setEnabled(true);
                        Snackbar.make(findViewById(android.R.id.content), o, Snackbar.LENGTH_LONG).show();
                    }
                });

            }
        });*/

        startRxOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                subscription = Single.create(new Single.OnSubscribe<String>() {
                    @Override
                    public void call(SingleSubscriber<? super String> singleSubscriber) {
                        String value = longRunningOperation();
                        singleSubscriber.onSuccess(value);
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {

                            @Override
                            public void call(String s) {
                                v.setEnabled(true);
                                Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG).show();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // handle onError
                            }
                        });

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* CompositeSubscription tes = new CompositeSubscription() ;
        tes.add(subscription);
        tes.unsubscribe();*/
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public String longRunningOperation() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // error
        }
        return "Complete!";
    }
}
