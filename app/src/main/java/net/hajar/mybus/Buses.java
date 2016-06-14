package net.hajar.mybus;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;

public class Buses
{
  private String busline;
  private String end;
  private Integer busnumber;
  private java.util.Date updated;
  private String ownerId;
  private String start;
  private String objectId;
  private java.util.Date created;
  private java.util.List<location> LSN;
  private java.util.List<Company> CSN;
  public String getBusline()
  {
    return busline;
  }

  public void setBusline( String busline )
  {
    this.busline = busline;
  }

  public String getEnd()
  {
    return end;
  }

  public void setEnd( String end )
  {
    this.end = end;
  }

  public Integer getBusnumber()
  {
    return busnumber;
  }

  public void setBusnumber( Integer busnumber )
  {
    this.busnumber = busnumber;
  }

  public java.util.Date getUpdated()
  {
    return updated;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getStart()
  {
    return start;
  }

  public void setStart( String start )
  {
    this.start = start;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public java.util.Date getCreated()
  {
    return created;
  }

  public java.util.List<location> getLSN()
  {
    return LSN;
  }

  public void setLSN( java.util.List<location> LSN )
  {
    this.LSN = LSN;
  }

  public java.util.List<Company> getCSN()
  {
    return CSN;
  }

  public void setCSN( java.util.List<Company> CSN )
  {
    this.CSN = CSN;
  }

                                                    
  public Buses save()
  {
    return Backendless.Data.of( Buses.class ).save( this );
  }

  public Future<Buses> saveAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Buses> future = new Future<Buses>();
      Backendless.Data.of( Buses.class ).save( this, future );

      return future;
    }
  }

  public void saveAsync( AsyncCallback<Buses> callback )
  {
    Backendless.Data.of( Buses.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Buses.class ).remove( this );
  }

  public Future<Long> removeAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Long> future = new Future<Long>();
      Backendless.Data.of( Buses.class ).remove( this, future );

      return future;
    }
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Buses.class ).remove( this, callback );
  }

  public static Buses findById(String id )
  {
    return Backendless.Data.of( Buses.class ).findById( id );
  }

  public static Future<Buses> findByIdAsync(String id )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Buses> future = new Future<Buses>();
      Backendless.Data.of( Buses.class ).findById( id, future );

      return future;
    }
  }

  public static void findByIdAsync( String id, AsyncCallback<Buses> callback )
  {
    Backendless.Data.of( Buses.class ).findById( id, callback );
  }

  public static Buses findFirst()
  {
    return Backendless.Data.of( Buses.class ).findFirst();
  }

  public static Future<Buses> findFirstAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Buses> future = new Future<Buses>();
      Backendless.Data.of( Buses.class ).findFirst( future );

      return future;
    }
  }

  public static void findFirstAsync( AsyncCallback<Buses> callback )
  {
    Backendless.Data.of( Buses.class ).findFirst( callback );
  }

  public static Buses findLast()
  {
    return Backendless.Data.of( Buses.class ).findLast();
  }

  public static Future<Buses> findLastAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Buses> future = new Future<Buses>();
      Backendless.Data.of( Buses.class ).findLast( future );

      return future;
    }
  }

  public static void findLastAsync( AsyncCallback<Buses> callback )
  {
    Backendless.Data.of( Buses.class ).findLast( callback );
  }

  public static BackendlessCollection<Buses> find(BackendlessDataQuery query )
  {
    return Backendless.Data.of( Buses.class ).find( query );
  }

  public static Future<BackendlessCollection<Buses>> findAsync(BackendlessDataQuery query )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<BackendlessCollection<Buses>> future = new Future<BackendlessCollection<Buses>>();
      Backendless.Data.of( Buses.class ).find( query, future );

      return future;
    }
  }

  public static void findAsync(BackendlessDataQuery query, AsyncCallback<BackendlessCollection<Buses>> callback )
  {
    Backendless.Data.of( Buses.class ).find( query, callback );
  }
}