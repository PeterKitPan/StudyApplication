package com.xs.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     *rxjava2.0
     */
//    private void rxjava2() {
//        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                emitter.onNext("1");
//                emitter.onNext("2");
//                emitter.onNext("3");
//                emitter.onComplete();
//            }
//        });
//
//        Observer<String> observer = new Observer<String>() {
//            private Disposable mDisposable;
//
//            @Override
//            public void onSubscribe(Disposable d) {
//                mDisposable = d;
//                Log.e("MSG","onSubscribe");
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.e("MSG",s);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e("MSG","onError");
//                mDisposable.dispose();
//            }
//
//            @Override
//            public void onComplete() {
//                Log.e("MSG","onComplete");
//                mDisposable.dispose();
//            }
//        };
//
//        observable.subscribe(observer);
//
//    }

    /**
     * rxjava1.0
     */
//    private void rxjava1() {
//        final int imageRes = 0;
//        final ImageView imageView = new ImageView(this);
//        Subscriber<Drawable> subscriber = new Subscriber<Drawable>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Drawable drawable) {
//                imageView.setImageDrawable(drawable);
//            }
//        };
//        Observable.create(new Observable.OnSubscribe<Drawable>() {
//            @Override
//            public void call(Subscriber<? super Drawable> subscriber) {
//                Drawable drawable = getTheme().getDrawable(imageRes);
//                subscriber.onNext(drawable);
////                subscriber.onComplete();
//                subscriber.onCompleted();
//            }
//        }).subscribe(new Subscriber<Drawable>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Drawable drawable) {
//                imageView.setImageDrawable(drawable);
//            }
//        });
//
//
//
////        Observable.create(new ObservableOnSubscribe<Drawable>() {
////
////            @Override
////            public void subscribe(@NonNull ObservableEmitter<Drawable> emitter) throws Throwable {
////                Drawable drawable = getTheme().getDrawable(imageRes);
////                emitter.onNext(drawable);
////                emitter.onComplete();
////            }
////        }).subscribe(new Observer<Drawable>() {
////            @Override
////            public void onSubscribe(@NonNull Disposable d) {
////
////            }
////
////            @Override
////            public void onNext(@NonNull Drawable drawable) {
////                imageView.setImageDrawable(drawable);
////            }
////
////            @Override
////            public void onError(@NonNull Throwable e) {
////
////            }
////
////            @Override
////            public void onComplete() {
////
////            }
////        });
//
//    }

}
