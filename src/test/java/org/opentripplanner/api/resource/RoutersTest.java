package org.opentripplanner.api.resource;

import org.junit.Test;
import org.opentripplanner.api.model.RouterInfo;
import org.opentripplanner.api.model.RouterList;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.impl.GraphServiceImpl;
import org.opentripplanner.routing.vertextype.ExitVertex;
import org.opentripplanner.standalone.CommandLineParameters;
import org.opentripplanner.standalone.OTPServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RoutersTest {
    @Test
    public void testRouters() {
        OTPServer otpServer = new OTPServer(new CommandLineParameters(), new GraphServiceImpl());
        otpServer.graphService.registerGraph(null, new Graph());
        otpServer.graphService.registerGraph("", new Graph());
        otpServer.graphService.getGraph("").addVertex(new ExitVertex(null, "A", 0, 0));
        otpServer.graphService.getGraph("").addVertex(new ExitVertex(null, "B", 0, 1));
        otpServer.graphService.getGraph("").addVertex(new ExitVertex(null, "C", 1, 1));

        Routers routerApi = new Routers();
        routerApi.server = otpServer;
        RouterList routers = routerApi.getRouterIds();
        assertEquals(2, routers.routerInfo.size());
        RouterInfo router0 = routers.routerInfo.get(0);
        RouterInfo router1 = routers.routerInfo.get(1);
        RouterInfo otherRouter;
        RouterInfo defaultRouter;
        if (router0.routerId == null) {
            defaultRouter = router0;
            otherRouter = router1;
        } else {
            defaultRouter = router1;
            otherRouter = router0;
        }
        assertNull(defaultRouter.routerId);
        assertNotNull(otherRouter.routerId);
        assertTrue(otherRouter.polygon.getArea() > 0);
    }
}
