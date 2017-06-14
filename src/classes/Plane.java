package classes;

public class Plane 
{

	private float speedX;
	private float speedY;
	private float direction;
	private float horodatageSpeed;
	private float horodatageGeolocalisation;
	private Geolocation geolocation;
	private boolean grounded;
	
	
	public Plane()
	{
		this.speedX=-1;
		this.speedY=-1;
		this.direction=-1;
		this.horodatageGeolocalisation=-1;
		this.horodatageSpeed=-1;
		this.grounded=true;
		this.geolocation=new Geolocation(-1,-1, -1);
	}
	public Plane(Geolocation geolocation, float speedX,float direction, float horodatageGeolocation,float horodatageSpeed,float speedY, boolean grounded) 
	{
		this.speedX=speedX;
		this.speedY=speedY;
		this.direction=direction;
		this.horodatageGeolocalisation=horodatageGeolocation;
		this.horodatageSpeed=horodatageSpeed;
		this.grounded=grounded;
		this.geolocation=geolocation;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Plane [speedX=" + speedX + ", speedY=" + speedY + ", direction=" + direction + ", horodatageSpeed="
				+ horodatageSpeed + ", horodatageGeolocalisation=" + horodatageGeolocalisation + ", geolocation="
				+ geolocation + ", grounded=" + grounded + "]";
	}

	/**
	 * @param speedX the speedX to set
	 */
	public void setSpeedX(float speedX) 
	{
		this.speedX = speedX;
	}
	/**
	 * @param speedY the speedY to set
	 */
	public void setSpeedY(float speedY) 
	{
		this.speedY = speedY;
	}
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(float direction) 
	{
		this.direction = direction;
	}
	/**
	 * @param horodatageSpeed the horodatageSpeed to set
	 */
	public void setHorodatageSpeed(float horodatageSpeed) 
	{
		this.horodatageSpeed = horodatageSpeed;
	}
	/**
	 * @param horodatageGeolocalisation the horodatageGeolocalisation to set
	 */
	public void setHorodatageGeolocalisation(float horodatageGeolocalisation) 
	{
		this.horodatageGeolocalisation = horodatageGeolocalisation;
	}
	/**
	 * @param geolocation the geolocation to set
	 */
	public void setGeolocation(Geolocation geolocation) 
	{
		this.geolocation = geolocation;
	}
	/**
	 * @param grounded the grounded to set
	 */
	public void setGrounded(boolean grounded) 
	{
		this.grounded = grounded;
	}
	public Geolocation getGeolocation() {

		return geolocation;
	}

	/**
	 * @return the model
	 */
	
	
	
	

}
