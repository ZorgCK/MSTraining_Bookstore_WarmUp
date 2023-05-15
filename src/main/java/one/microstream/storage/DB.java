package one.microstream.storage;

import java.time.Duration;

import one.microstream.domain.Root;
import one.microstream.reference.Lazy;
import one.microstream.reference.LazyReferenceManager;
import one.microstream.storage.embedded.configuration.types.EmbeddedStorageConfiguration;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;


public class DB
{
	public static EmbeddedStorageManager	storageManager;
	public final static Root				root	= new Root();
	
	static
	{
		LazyReferenceManager.set(LazyReferenceManager.New(
			Lazy.Checker(Duration.ofMinutes(10).toMillis())
			)).start();
		
		storageManager = EmbeddedStorageConfiguration.Builder()
			.setChannelCount(2)
			.setStorageDirectory("data")
			.setBackupDirectory("backup")
			.createEmbeddedStorageFoundation()
			.createEmbeddedStorageManager(root)
			.start();
	}
}
