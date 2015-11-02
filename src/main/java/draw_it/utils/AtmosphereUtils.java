package draw_it.utils;

import org.atmosphere.cpr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CountDownLatch;

@Service(value = "atmosphereUtils")
public final class AtmosphereUtils {

    public static final Logger LOG = LoggerFactory.getLogger(AtmosphereUtils.class);

    private final BroadcasterFactory broadcasterFactory;

    private final MetaBroadcaster metaBroadcaster;

    public AtmosphereUtils() {
        broadcasterFactory = BroadcasterFactory.getDefault();
        metaBroadcaster = MetaBroadcaster.getDefault();
    }

    public BroadcasterFactory getBroadcasterFactory() {
        return broadcasterFactory;
    }

    public MetaBroadcaster getMetaBroadcaster() {
        return metaBroadcaster;
    }

    public AtmosphereResource getAtmosphereResource(HttpServletRequest request) {
        return getMeteor(request).getAtmosphereResource();
    }

    public Meteor getMeteor(HttpServletRequest request) {
        return Meteor.build(request);
    }

    public void suspend(final String broadcasterName, final AtmosphereResource resource) {

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        resource.addEventListener(new AtmosphereResourceEventListenerAdapter() {
            @Override
            public void onSuspend(AtmosphereResourceEvent event) {
                countDownLatch.countDown();
                LOG.info("Suspending Client..." + resource.uuid());
                Broadcaster broadcaster = broadcasterFactory.lookup(broadcasterName, true);
                resource.setBroadcaster(broadcaster);
                broadcaster.addAtmosphereResource(resource);
                resource.removeEventListener(this);

                resource.getRequest().getSession().setAttribute("userLogin", SecurityUtils.getCurrentUser().getLogin());
            }

            @Override
            public void onDisconnect(AtmosphereResourceEvent event) {
                LOG.info("Disconnecting Client..." + resource.uuid());
                broadcasterFactory.lookup(broadcasterName).removeAtmosphereResource(resource);
                super.onDisconnect(event);

                // TODO stuff
            }

            @Override
            public void onBroadcast(AtmosphereResourceEvent event) {
                LOG.info("Client is broadcasting..." + resource.uuid());
                super.onBroadcast(event);
            }

        });

        if (AtmosphereResource.TRANSPORT.LONG_POLLING.equals(resource.transport())) {
            resource.resumeOnBroadcast(true).suspend(-1);
        } else {
            resource.suspend(-1);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOG.error("Interrupted while trying to suspend resource {}", resource);
        }
    }
}
