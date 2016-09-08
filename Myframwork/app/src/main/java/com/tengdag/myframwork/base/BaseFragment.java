package com.tengdag.myframwork.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public abstract class BaseFragment extends Fragment {


    // TODO: Rename and change types of parameters
    protected Bundle mBundle;

    private OnFragmentInteractionListener mListener;
    public BaseFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bundle 从Activity传入参数
     * @return A new instance of fragment BaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public BaseFragment newInstance(BaseFragment fragment,Bundle bundle) {
        if (bundle!=null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    public static BaseFragment getInstance(Activity activity, String name) {
        BaseFragment instance = (BaseFragment) instantiate(activity, name);
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBundle = getArguments();
        }
    }

    public Bundle getMbundle() {
        return mBundle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        System.gc();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * 回调函数
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        //  Update argument type and name
        void onFragmentInteraction(Object... obj);
    }

    private boolean isAppActivity(){
        if(null != getActivity() || getActivity() instanceof BaseActivity){
            return true;
        }
        return false;
    }

    public void showWaitingProgress() {
        if(isAppActivity()) {
            ((BaseActivity) getActivity()).showWaitingProgress();
        }
    }
    public void showProgress(String title) {
        if(isAppActivity()) {
            ((BaseActivity) getActivity()).showProgress(title);
        }
    }

    public void closeProgress() {
        if(isAppActivity()) {
            ((BaseActivity) getActivity()).closeProgress();
        }
    }


    public void toast(String text) {
        if(isAppActivity()) {
            Toast.makeText(((BaseActivity) getActivity()),text,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideSoftKeyboard();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public void hideSoftKeyboard() {
        View v = getActivity().getCurrentFocus();
        if (null == v) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    public void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
