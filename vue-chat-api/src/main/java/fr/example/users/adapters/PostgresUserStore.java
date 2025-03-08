package fr.example.users.adapters;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertResultStep;
import org.jooq.SelectWhereStep;
import org.springframework.stereotype.Repository;

import fr.example.database.tables.Users;
import fr.example.database.tables.records.UsersRecord;
import fr.example.users.User;
import fr.example.users.UserStore;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PostgresUserStore implements UserStore {
	
	private final DSLContext reactiveDslContext;

	@Override
	public Flux<User> listUsers() {
		SelectWhereStep<UsersRecord> query = reactiveDslContext.selectFrom(Users.USERS);
		return Flux.from(query).map(u -> new User(u.getId(), u.getName()));
	}

	@Override
	public Mono<User> createUser(User user) {
		InsertResultStep<UsersRecord> query = reactiveDslContext.insertInto(Users.USERS, Users.USERS.NAME)
				.values(List.of(user.getName()))
				.returning();
		return Mono.from(query).map(u -> new User(u.getId(), u.getName()));
	}

}
