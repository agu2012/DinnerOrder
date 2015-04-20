package com.bocs.dodemo.fragment;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bocs.dodemo.activity.R;
import com.bocs.dodemo.bussiness.DinnerOrderManager;
import com.bocs.dodemo.bussiness.impl.DinnerOrderManagerImpl;
import com.bocs.dodemo.entity.Employee;
import com.bocs.dodemo.entity.EmployeeLab;

public class EmployeeListFragment extends ListFragment implements
		View.OnClickListener {

	private static final String TAG = "==EmployeeListFragment==";
	private TextView tv_title;
	private Button btn_next;
	private ListView listview;

	// private List<Employee> employees = new ArrayList<Employee>();
	private LayoutInflater listContainer; // 视图容器
	private DinnerOrderManager dom;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dom = new DinnerOrderManagerImpl(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_employee, container, false);

		tv_title = (TextView) v.findViewById(R.id.tv_title);
		tv_title.setText("员工管理");

		btn_next = (Button) v.findViewById(R.id.btn_next);
		btn_next.setText("新增");
		btn_next.setOnClickListener(this);

		listview = (ListView) v.findViewById(android.R.id.list);
		EmployeeAdapter adapter = new EmployeeAdapter(EmployeeLab.get(
				getActivity()).getEmployees());
		listview.setAdapter(adapter);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:// Add new employee
			addNewEmployee();
			break;
		}
	}

	public void addNewEmployee() {

		final EditText et = new EditText(getActivity());

		// et.setInputType(0x81);
		// et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });

		new AlertDialog.Builder(getActivity())
				.setTitle("新员工录入")
				.setMessage("请输入员工姓名：")
				.setView(et)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setNegativeButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (!TextUtils.isEmpty(et.getText())) {
									Employee employee = new Employee();
									employee.setEmployeeName(et.getText()
											.toString());
									dom.addEmployee(employee);
									EmployeeLab.get(
											getActivity()).getEmployees().add(employee);
									EmployeeAdapter adapter = (EmployeeAdapter) listview
											.getAdapter();
									adapter.notifyDataSetChanged();
								}
							}

						})
				.setPositiveButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();

	}

	private class EmployeeAdapter extends BaseAdapter {
		List<Employee> employees;

		public EmployeeAdapter(List<Employee> _employees) {
			employees = _employees;
			listContainer = LayoutInflater.from(getActivity()); // 创建视图容器并设置上下文
		}

		@Override
		public int getCount() {
			return employees.size();
		}

		@Override
		public Employee getItem(int position) {
			return employees.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ListViewItem listItemView = null;
			if (convertView == null) {
				listItemView = new ListViewItem();
				// 获取list_item布局文件的视图
				convertView = listContainer
						.inflate(R.layout.row_employee, null);
				// 获取控件对象
				listItemView.row_employee_tv = (TextView) convertView
						.findViewById(R.id.row_employee_tv);
				listItemView.row_employee_btn_delete = (Button) convertView
						.findViewById(R.id.row_employee_btn_delete);
				convertView.setTag(listItemView);
			} else {
				listItemView = (ListViewItem) convertView.getTag();
			}

			if (getItem(position) != null) {
				listItemView.row_employee_tv.setText(getItem(position)
						.getEmployeeName());
				listItemView.row_employee_btn_delete
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								EmployeeAdapter adapter = (EmployeeAdapter) listview
										.getAdapter();

								dom.deleteEmployee(getItem(position));
								employees.remove(position);// TODO gxy-会删除LAb里面的值吗？
								
								adapter.notifyDataSetChanged();
							}
						});
			}
			return convertView;
		}

		public final class ListViewItem { // 自定义控件集合
			public TextView row_employee_tv;
			public Button row_employee_btn_delete;
		}

	}
}
