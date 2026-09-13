import { Injectable } from '@angular/core';
import {
  Firestore,
  collection,
  getDocs,
} from '@angular/fire/firestore';
import { Quiz } from '../models/quiz/Quiz';
import { GAME_SEEDS } from '../utils/Constants';
import { QuizService } from './quiz.service';

@Injectable({
  providedIn: 'root',
})
export class GameSeedService {
  constructor(
    private firestore: Firestore,
    private quizService: QuizService
  ) {}

  async seedGamesOnce(): Promise<{
    created: Quiz[];
    skipped: number;
    total: number;
  }> {
    const snapshot = await getDocs(collection(this.firestore, 'quiz'));
    const existingTitles = new Set(
      snapshot.docs
        .map((doc) => (doc.data() as Quiz).title?.trim().toLowerCase())
        .filter((title): title is string => !!title)
    );

    const gamesToSeed = GAME_SEEDS.filter((game) => {
      const normalizedTitle = game.title.trim().toLowerCase();
      return !existingTitles.has(normalizedTitle);
    });

    const created: Quiz[] = [];

    for (const game of gamesToSeed) {
      const newId = await this.quizService.createQuiz(game);

      if (newId) {
        created.push({ ...game, id: newId });
      }
    }

    return {
      created,
      skipped: GAME_SEEDS.length - gamesToSeed.length,
      total: GAME_SEEDS.length,
    };
  }
}
