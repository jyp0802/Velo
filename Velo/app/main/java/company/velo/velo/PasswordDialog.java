package company.velo.velo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jyp08 on 2017-07-23.
 */

public class PasswordDialog extends DialogFragment {
    private static final String PSWD = "pswd";

    public static PasswordDialog newDialog (String pswd) {
        PasswordDialog pd = new PasswordDialog();
        Bundle args = new Bundle();
        args.putString(PSWD, pswd);
        pd.setArguments(args);
        return pd;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmentStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        View rootView = inflater.inflate(R.layout.password_dialog, container, false);
        getDialog().setTitle("Password:");
        TextView tv = (TextView) rootView.findViewById(R.id.psw_tv);
        tv.setText(args.getString(PSWD));
        return rootView;
    }
}
