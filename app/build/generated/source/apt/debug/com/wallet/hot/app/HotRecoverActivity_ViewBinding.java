// Generated code from Butter Knife. Do not modify!
package com.wallet.hot.app;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.wallet.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HotRecoverActivity_ViewBinding implements Unbinder {
  private HotRecoverActivity target;

  private View view2131165253;

  @UiThread
  public HotRecoverActivity_ViewBinding(HotRecoverActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HotRecoverActivity_ViewBinding(final HotRecoverActivity target, View source) {
    this.target = target;

    View view;
    target.mEtData = Utils.findRequiredViewAsType(source, R.id.et_data_inprot, "field 'mEtData'", EditText.class);
    target.mEtPwd = Utils.findRequiredViewAsType(source, R.id.et_pwd, "field 'mEtPwd'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btn_inprot_mnemonic, "method 'onClickInportMnemonic'");
    view2131165253 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickInportMnemonic();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    HotRecoverActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mEtData = null;
    target.mEtPwd = null;

    view2131165253.setOnClickListener(null);
    view2131165253 = null;
  }
}
