package sunxl8.android_lib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.RxFragment;


/**
 * Created by sunxl8 on 2017/11/30.
 */

public abstract class BaseFragment extends RxFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(setContentViewId(), container, false);
        init(view);
        initView();
        initData();
        return view;
    }

    protected abstract int setContentViewId();

    protected abstract void init(View view);

    protected abstract void initView();

    protected abstract void initData();
}
