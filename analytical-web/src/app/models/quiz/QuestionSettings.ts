import { QueryDocumentSnapshot } from '@angular/fire/firestore';

export const QUESTION_SETTINGS_COLLECTION = 'QuestionSettings';
export const QUESTION_SETTINGS_DOCUMENT = 'questions';

export interface QuestionSettings {
  rebus_puzzle_ids: string[];
  riddles_ids: string[];
  word_puzzle_ids: string[];
  math_puzzle_ids: string[];
}

export const questionSettingsConverter = {
  toFirestore: (data: QuestionSettings) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => {
    const quiz = snap.data() as QuestionSettings;
    return quiz;
  },
};

export function getQuestionType(
  type: 'REBUS_PUZZLE' | 'RIDDLES' | 'WORD_PUZZLE' | 'MATH_LOGIC_PUZZLE'
): string {
  switch (type) {
    case 'REBUS_PUZZLE':
      return 'rebus_puzzle_ids';
    case 'RIDDLES':
      return 'riddles_ids';
    case 'WORD_PUZZLE':
      return 'word_puzzle_ids';
    case 'MATH_LOGIC_PUZZLE':
      return 'math_puzzle_ids';
    default:
      return 'rebus_puzzle_ids';
  }
}
