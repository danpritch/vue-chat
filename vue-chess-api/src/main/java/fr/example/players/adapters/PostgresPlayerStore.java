package fr.example.players.adapters;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertResultStep;
import org.jooq.SelectWhereStep;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import fr.example.database.tables.Players;
import fr.example.database.tables.records.PlayersRecord;
import fr.example.players.Player;
import fr.example.players.PlayerStore;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Primary
@Repository
@RequiredArgsConstructor
public class PostgresPlayerStore implements PlayerStore {
	
	private final DSLContext dslContext;

	@Override
	public Flux<Player> listPlayers() {
		SelectWhereStep<PlayersRecord> query = dslContext.selectFrom(Players.PLAYERS);
		return Flux.from(query).map(p -> new Player(p.getId(), p.getName()));
	}

	@Override
	public Mono<Player> createPlayer(Player player) {
		InsertResultStep<PlayersRecord> query = dslContext.insertInto(Players.PLAYERS, Players.PLAYERS.NAME)
				.values(List.of(player.getName()))
				.returning();
		return Mono.from(query).map(p -> new Player(p.getId(), p.getName()));
	}

}
