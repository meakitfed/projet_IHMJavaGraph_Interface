/**
 * 
 */
package classes;

/**
 * @author nathan.bonnard
 *
 */
public class Geolocation 
{
	private float latitude;
	private float longitude;
	private float height;
	private static final float DIST_LANDING = 1.f;

	
	public Geolocation(float longitude,float latitude, float height) 
	{
		this.latitude=latitude;
		this.longitude=longitude;
		this.height = height;
		// TODO Auto-generated constructor stub
	}

	public float getLatitude() 
	{
		return latitude;
	}

	public void setLatitude(float latitude) 
	{
		this.latitude = latitude;
	}

	public float getLongitude()
	{
		return longitude;
	}

	public void setLongitude(float longitude) 
	{
		this.longitude = longitude;
	}

	/**
	 * @return the height
	 */
	public float getHeight() 
	{
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) 
	{
		this.height = height;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return "Geolocation [latitude=" + latitude + ", longitude=" + longitude + ", height=" + height + "]";
	}
	
	public float distanceTo(Geolocation other)
	{
		return (float) Math.sqrt((this.latitude-other.latitude)*(this.latitude-other.latitude) + (this.longitude-other.longitude)*(this.longitude-other.longitude)); 
	}
	
	public boolean isCloseTo(Geolocation other)
	{
		if (this.distanceTo(other) < DIST_LANDING) return true;
		else return false;
	}
	
	

}
