package de.mq.phone.domain.person.support;

import java.math.BigDecimal;

import org.springframework.util.Assert;

import de.mq.phone.domain.person.GeoCoordinates;
import de.mq.vaadin.util.BeforeSave;



class GeoDegreesCoordinatesImpl implements GeoCoordinates {
	
	
	private final Double latitude;
	private final Double longitude;
	
	@SuppressWarnings("unused")
	private Double[] location;
	
	
	GeoDegreesCoordinatesImpl(final double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@SuppressWarnings("unused")
	private GeoDegreesCoordinatesImpl() {
		this(0,0);
	}
	
	 /* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.GeoCoordinates#latitude()
	 */
	@Override
	public final Double latitude(){
		 Assert.notNull(latitude);
		 return latitude;
	 }
	
	 /* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.GeoCoordinates#longitude()
	 */
	@Override
	public final Double longitude() {
		 Assert.notNull(longitude);
		 return longitude;
	 }
	
	
	 
	 @BeforeSave
	 void beforeSave() {
		 this.location=new Double[]{latitude(), longitude()};
	 }
	 
	 
	 int[]  degrees(final double value) {
		
		 BigDecimal val= BigDecimal.valueOf(Math.abs(value));
		 int result[] =new int[3];
		 for(int i=0; i <= 2 ; i++){
			 result[i] = val.intValue() ;
		     val = (val.subtract(BigDecimal.valueOf(result[i]) ).multiply(BigDecimal.valueOf(60))) ;
		    
		 }
		
		 result[2] = (int) Math.round( result[2] + val.divide(BigDecimal.valueOf(60)).doubleValue());
		 return result;
		 
	 }
	 @Override
	 public final String location() {
		 Assert.notNull(latitude);
		 Assert.notNull(longitude);
		 String lat = "N";
		 if( Math.signum(latitude)<0 ) {
			 lat="S";
		 }
		 String lon = "E";
		 if( Math.signum(longitude)<0 ) {
			 lon="W";
		 }
		 final int[] x=degrees(latitude);
		 final int[] y=degrees(longitude);
		 return String.format("%s %s° %s' %s'' %s %s° %s' %s''", lat,x[0] , x[1],  x[2], lon, y[0] , y[1],  y[2]  );
	
		
	 }

	@Override
	public int hashCode() {
		if( ! valid(this)){
			return super.hashCode();
		}
		return longitude.hashCode() + latitude.hashCode();
		
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GeoDegreesCoordinatesImpl)) {
			return false;	
		}
		if( ! valid(this)){
			return super.equals(obj);
		}
		GeoDegreesCoordinatesImpl other = (GeoDegreesCoordinatesImpl) obj; 
		if( ! valid(other)){
			return super.equals(obj);
		}
		return latitude.equals(other.latitude) && longitude.equals(other.longitude);		
	}

	private boolean  valid(GeoDegreesCoordinatesImpl geoCoordinates) {
		if( geoCoordinates.longitude==null){
			return false;
		}
		if( geoCoordinates.latitude==null){
			return false; 
		}
		return true;
	}
	 
	 
}
