package project.java.tbusdriver.Controller.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.java.tbusdriver.R;

////////////////////////////////////////////////////////////////////
/////////////////////////// NOT IN USE ////////////////////////////
////////////////////////////////////////////////////////////////////
///don't know what this class should do. probably adapter
public class RideView extends Fragment {

    View myView;

    FragmentActivity myActivity;

    private OnFragmentInteractionListener mListener;

    public RideView() {
        // Required empty public constructor
    }


    //public static RideView newInstance(String param1, String param2) {
    //    RideView fragment = new RideView();
    //    Bundle args = new Bundle();
    //    args.putString(ARG_PARAM1, param1);
    //    args.putString(ARG_PARAM2, param2);
    //    fragment.setArguments(args);
    //    return fragment;
    //}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_ride_view, container, false);
        //int totalHeight=0;
        //ViewGroup.LayoutParams params = sem0.getLayoutParams();
        //params.height = totalHeight + (sem0.getDividerHeight() );
        //sem0.setLayoutParams(params);
        //sem0.requestLayout();

        return myView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
