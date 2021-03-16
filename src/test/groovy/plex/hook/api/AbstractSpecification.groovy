package plex.hook.api

import plex.hook.api.domains.PlexEvent
import plex.hook.api.models.PlexAccount
import plex.hook.api.models.PlexMetadata
import plex.hook.api.models.PlexServer
import plex.hook.api.models.PlexWebhook
import spock.lang.Specification

abstract class AbstractSpecification extends Specification {

    protected static PlexWebhook getPlexWebhook(Map args = [:]) {
        def event = args.get("event", PlexEvent.PLAY) as PlexEvent
        def account = new PlexAccount(args.get("id", 1l), args.get("accountTitle", "The Show"))
        def server = new PlexServer(args.get("serverTitle", "plex-server"), args.get("serverUuid", "plex-server"))
        def metadata = new PlexMetadata(
                args.get("metadataTile", "The Finale"),
                args.get("metadataType", "episode"),
                args.get("metadataYear", 2020) as Integer,
                args.get("metadataGrandparentTitle", "The Show")
        )
        return new PlexWebhook(event, account, server, metadata)
    }
}
