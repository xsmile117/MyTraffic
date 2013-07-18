package net.xsmile.myTraffic;

import android.os.Parcel;
import android.os.Parcelable;


public class VehicleBreak implements Parcelable {
	private String monitorType;//������
	private String breakTime;//Υ��ʱ��
	private String breakPlace;//Υ�µص�
	private String breakType;//Υ����Ϊ
	private String breakPicId;//ͼƬΥ�±�ţ��������ݿ����
	private String breakTypeId;//Υ����ϸ��ţ��������ݿ����
	private String breakTypeDetail;//Υ����ϸ����
	private String breakMoney;//����
	private String breakScore;//�۷�
	
	public VehicleBreak(){
		super();
		this.monitorType = "";
		this.breakTime = "";
		this.breakPlace = "";
		this.breakType = "";
		this.breakPicId = "";
		this.breakTypeDetail = "";
		this.breakMoney = "";
		this.breakScore = "";
		this.breakTypeId="";
	}
	

	public String getBreakPicId() {
		return breakPicId;
	}

	public void setBreakPicId(String breakPicId) {
		this.breakPicId = breakPicId;
	}

	public String getBreakTypeId() {
		return breakTypeId;
	}

	public void setBreakTypeId(String breakTypeId) {
		this.breakTypeId = breakTypeId;
	}

	public void setBreakMoney(String breakMoney) {
		this.breakMoney = breakMoney;
	}
	
	public String getMonitorType() {
		return monitorType;
	}
	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}
	public String getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}
	public String getBreakPlace() {
		return breakPlace;
	}
	public void setBreakPlace(String breakPlace) {
		this.breakPlace = breakPlace;
	}
	public String getBreakType() {
		return breakType;
	}
	public void setBreakType(String breakType) {
		this.breakType = breakType;
	}
	
	public String getBreakTypeDetail() {
		return breakTypeDetail;
	}
	public void setBreakTypeDetail(String breakTypeDetail) {
		this.breakTypeDetail = breakTypeDetail;
	}
	public String getBreakMoney() {
		return breakMoney;
	}

	public String getBreakScore() {
		return breakScore;
	}
	public void setBreakScore(String breakScore) {
		this.breakScore = breakScore;
	}
	
	
	public static final Parcelable.Creator<VehicleBreak> CREATOR =
		new Parcelable.Creator<VehicleBreak>() {
		@Override
		public VehicleBreak createFromParcel(Parcel in) {
			return new VehicleBreak(in);
		}
		@Override
		public VehicleBreak[] newArray(int size) {
			return new VehicleBreak[size];
		}
	};
		
	private VehicleBreak(Parcel in) {
		readFromParcel(in);
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flag) {
		// TODO Auto-generated method stub
		parcel.writeString(monitorType);
		parcel.writeString(breakTime);
		parcel.writeString(breakPlace);
		parcel.writeString(breakType);
		parcel.writeString(breakPicId);
		parcel.writeString(breakTypeDetail);
		parcel.writeString(breakMoney);
		parcel.writeString(breakScore);
		parcel.writeString(breakTypeId);
	}
	
	public void readFromParcel(Parcel in) {
		monitorType = in.readString();
		breakTime = in.readString();
		breakPlace = in.readString();
		breakType = in.readString();
		breakPicId = in.readString();
		breakTypeDetail = in.readString();
		breakMoney = in.readString();
		breakScore = in.readString();
		breakTypeId = in.readString();
	}


}
