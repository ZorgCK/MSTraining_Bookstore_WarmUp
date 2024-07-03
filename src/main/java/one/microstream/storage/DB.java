package one.microstream.storage;

import java.time.Duration;

import org.eclipse.serializer.reference.Lazy;
import org.eclipse.serializer.reference.LazyReferenceManager;
import org.eclipse.store.storage.embedded.configuration.types.EmbeddedStorageConfiguration;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;

import one.microstream.domain.Root;


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
