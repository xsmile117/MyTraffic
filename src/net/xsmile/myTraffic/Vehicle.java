package net.xsmile.myTraffic;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


public class Vehicle implements Parcelable {
	private String vehicleType;//车辆种类
	private String vehicleNum;//车牌号码
	private String vehicleId;//车辆识别码
	private List<VehicleBreak> vehiclebreaks;//违章信息
	private String lastCheckDate;//最近一次检查时间
	private String lastBreakDate;//最近一次违章时间
	private String startTime;//违章起始时间
	private String endTime;//违章结束时间

	public Vehicle() {
		super();
		this.vehicleType = "";
		this.vehicleNum = "";
		this.vehicleId = "";
		this.vehiclebreaks=new ArrayList<VehicleBreak>();
		this.lastBreakDate="1900-1-1 00:00:00";
		this.lastCheckDate="1900-1-1";
		this.startTime="0";
		this.endTime="0";
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getLastCheckDate() {
		return lastCheckDate;
	}

	public void setLastCheckDate(String lastCheckDate) {
		this.lastCheckDate = lastCheckDate;
	}

	public String getLastBreakDate() {
		return lastBreakDate;
	}

	public void setLastBreakDate(String lastBreakDate) {
		this.lastBreakDate = lastBreakDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<VehicleBreak> getVehiclebreaks() {
		return vehiclebreaks;
	}

	public void setVehiclebreaks(List<VehicleBreak> vehiclebreaks) {
		this.vehiclebreaks = vehiclebreaks;
	}

	
	
	
	public static final Parcelable.Creator<Vehicle> CREATOR =
		new Parcelable.Creator<Vehicle>() {
		@Override
		public Vehicle createFromParcel(Parcel in) {
			return new Vehicle(in);
		}
		@Override
		public Vehicle[] newArray(int size) {
			return new Vehicle[size];
		}
	};
		
	private Vehicle(Parcel in) {
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
		parcel.writeString(vehicleType);
		parcel.writeString(vehicleNum);
		parcel.writeString(vehicleId);
		parcel.writeString(lastBreakDate);
		parcel.writeString(lastCheckDate);
		parcel.writeString(startTime);
		parcel.writeString(endTime);
	}
	
	public void readFromParcel(Parcel in) {
		vehicleType = in.readString();
		vehicleNum = in.readString();
		vehicleId = in.readString();
		lastBreakDate = in.readString();
		lastCheckDate = in.readString();
		startTime = in.readString();
		endTime = in.readString();
	}



}