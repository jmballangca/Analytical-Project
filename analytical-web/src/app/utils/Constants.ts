import { Quiz } from '../models/quiz/Quiz';

export const GAMES = 'qXOTZgcJN3RPYgoNV27H';
export const GAMES_COLLECTION = 'games';

export const GAME_SEEDS_BY_CATEGORY: Record<Quiz['category'], Quiz[]> = {
  QUIZ_GAME: [
    {
      id: '',
      title: 'Algebra Sprint',
      desc: 'Test your speed and accuracy with quick algebra challenges.',
      cover_photo:
        'https://images.unsplash.com/photo-1509228468518-180dd4864904?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'SHS',
      category: 'QUIZ_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Grammar Quest',
      desc: 'Improve grammar confidence through short, focused rounds.',
      cover_photo:
        'https://images.unsplash.com/photo-1522202176988-66273c2fd55f?auto=format&fit=crop&w=900&q=80',
      subject: 'ENGLISH',
      schoolLevel: 'SHS',
      category: 'QUIZ_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Word Power',
      desc: 'Boost vocabulary and meaning with engaging quiz rounds.',
      cover_photo:
        'https://images.unsplash.com/photo-1455390582262-044cdead277a?auto=format&fit=crop&w=900&q=80',
      subject: 'ENGLISH',
      schoolLevel: 'JHS',
      category: 'QUIZ_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Number Ninja',
      desc: 'Beat the clock with arithmetic, patterns, and number logic.',
      cover_photo:
        'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'JHS',
      category: 'QUIZ_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Reading Rush',
      desc: 'Scan, choose, and answer fast to clear each reading challenge.',
      cover_photo:
        'https://images.unsplash.com/photo-1516979187454-437ec2e8dbd0?auto=format&fit=crop&w=900&q=80',
      subject: 'ENGLISH',
      schoolLevel: 'SHS',
      category: 'QUIZ_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
  ],
  PUZZLE_GAME: [
    {
      id: '',
      title: 'Cipher Puzzle',
      desc: 'Decode clues and solve hidden messages one layer at a time.',
      cover_photo:
        'https://images.unsplash.com/photo-1532012197267-da84d127e765?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'SHS',
      category: 'PUZZLE_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Word Weave',
      desc: 'Connect letters and ideas to reveal the hidden answer.',
      cover_photo:
        'https://images.unsplash.com/photo-1517849845537-4d257902454a?auto=format&fit=crop&w=900&q=80',
      subject: 'ENGLISH',
      schoolLevel: 'JHS',
      category: 'PUZZLE_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Pattern Path',
      desc: 'Follow the sequence and spot the missing piece quickly.',
      cover_photo:
        'https://images.unsplash.com/photo-1503676260728-1c00da094a0b?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'JHS',
      category: 'PUZZLE_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Sentence Scramble',
      desc: 'Rebuild meaning from jumbled words and phrases.',
      cover_photo:
        'https://images.unsplash.com/photo-1455390582262-044cdead277a?auto=format&fit=crop&w=900&q=80',
      subject: 'ENGLISH',
      schoolLevel: 'SHS',
      category: 'PUZZLE_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Logic Labyrinth',
      desc: 'Use reasoning to escape each puzzle room and reach the finish.',
      cover_photo:
        'https://images.unsplash.com/photo-1515879218367-8466d910aaa4?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'SHS',
      category: 'PUZZLE_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
  ],
  MEMORY_GAME: [
    {
      id: '',
      title: 'Memory Match',
      desc: 'Pair the right cards to clear each challenge board.',
      cover_photo:
        'https://images.unsplash.com/photo-1516321165247-4aa89a48be28?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'JHS',
      category: 'MEMORY_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Word Recall',
      desc: 'Use sharp memory skills to find and repeat the correct words.',
      cover_photo:
        'https://images.unsplash.com/photo-1516321497487-e288fb19713f?auto=format&fit=crop&w=900&q=80',
      subject: 'ENGLISH',
      schoolLevel: 'SHS',
      category: 'MEMORY_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Shape Sequence',
      desc: 'Memorize visual patterns and track the next correct form.',
      cover_photo:
        'https://images.unsplash.com/photo-1555949963-aa79dcee981c?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'SHS',
      category: 'MEMORY_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Spelling Sprint',
      desc: 'Practice memory and spelling with repeated visual prompts.',
      cover_photo:
        'https://images.unsplash.com/photo-1503676260728-1c00da094a0b?auto=format&fit=crop&w=900&q=80',
      subject: 'ENGLISH',
      schoolLevel: 'JHS',
      category: 'MEMORY_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Fact Finder',
      desc: 'Remember the clues, match the facts, and beat each board.',
      cover_photo:
        'https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'JHS',
      category: 'MEMORY_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
  ],
  MATH_GAME: [
    {
      id: '',
      title: 'Rapid Calculus',
      desc: 'Solve quick math problems and keep your streak alive.',
      cover_photo:
        'https://images.unsplash.com/photo-1635070041078-e363dbe005cb?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'SHS',
      category: 'MATH_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Equation Escape',
      desc: 'Break each equation and move through the challenge path.',
      cover_photo:
        'https://images.unsplash.com/photo-1509228468518-180dd4864904?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'JHS',
      category: 'MATH_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Fraction Frenzy',
      desc: 'Work through fractions and mixed operations with precision.',
      cover_photo:
        'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'SHS',
      category: 'MATH_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Number Relay',
      desc: 'Pass through each numeric challenge in a fast-paced relay.',
      cover_photo:
        'https://images.unsplash.com/photo-1522202176988-66273c2fd55f?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'JHS',
      category: 'MATH_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
    {
      id: '',
      title: 'Problem Pursuit',
      desc: 'Track down each math problem and finish every stage.',
      cover_photo:
        'https://images.unsplash.com/photo-1555949963-aa79dcee981c?auto=format&fit=crop&w=900&q=80',
      subject: 'MATH',
      schoolLevel: 'SHS',
      category: 'MATH_GAME',
      levels: 5,
      visible: true,
      createdAt: new Date(),
    },
  ],
};

export const GAME_SEEDS: Quiz[] = [
  ...GAME_SEEDS_BY_CATEGORY.QUIZ_GAME,
  ...GAME_SEEDS_BY_CATEGORY.PUZZLE_GAME,
  ...GAME_SEEDS_BY_CATEGORY.MEMORY_GAME,
  ...GAME_SEEDS_BY_CATEGORY.MATH_GAME,
];

export function getNumberInString(str: string): number | null {
  const match = str.match(/\d+/);

  if (match) {
    return Number(match[0]);
  }
  return null;
}

export function displayFullname(first: string, middle: string, last: string) {
  return `${first} ${middle[0]}. ${last}`;
}

export const colors = [
  '#81323d',
  '#f19f9d',
  '#91cec2',
  '#383b65',
  '#8b7d7d',
  '#a58998',
  '#757397',
  '#474e95',
  '#4662b2',
  '#638a8d',
];
