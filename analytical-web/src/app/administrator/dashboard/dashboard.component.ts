import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { ChartData, ChartOptions, ChartType } from 'chart.js';
import { SubmissionsService } from '../../services/submissions.service';
import { QuizService } from '../../services/quiz.service';
import { combineLatest, map, Observable } from 'rxjs';
import { Submissions } from '../../models/submissions/Submissions';
import { Students } from '../../models/students/Student';

import { StudentWithSubmissions } from '../../models/students/StudentWithSubmissions';
import { AsyncPipe } from '@angular/common';
import { Quiz } from '../../models/quiz/Quiz';
import { GameWithSubmissions } from '../../models/quiz/Game';
import { BaseChartDirective } from 'ng2-charts';
import { displayFullname } from '../../utils/Constants';
import { Category } from '../../models/submissions/Category';
import { SchoolLevel } from '../../models/students/GradeLevel';

export interface StudentSubmissionWithGame {
  name: string;
  profile: string;
  gameName: string;
  levelName: string;
  earning: number;
}

interface AverageScorePerGame {
  category: 'MATH' | 'ENGLISH';
  averegePerSubmission: number;
  totalMacthes: number;
}
interface MostPlayedCategories {
  category: Category;
  total: number;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  private readonly placeholderRecentSubmissions: StudentSubmissionWithGame[] = [
    {
      name: 'Maria Santos',
      profile: '../../../../assets/images/profile.jpg',
      gameName: 'Algebra Sprint',
      levelName: 'Level 1',
      earning: 120,
    },
    {
      name: 'Jose Dela Cruz',
      profile: '../../../../assets/images/profile.jpg',
      gameName: 'Word Power',
      levelName: 'Level 2',
      earning: 140,
    },
    {
      name: 'Ariana Ramos',
      profile: '../../../../assets/images/profile.jpg',
      gameName: 'Memory Match',
      levelName: 'Level 3',
      earning: 160,
    },
  ];

  private readonly placeholderAverageScorePerGame = [
    { category: 'MATH' as const, averagePerSubmission: 76, totalMatches: 10 },
    { category: 'ENGLISH' as const, averagePerSubmission: 82, totalMatches: 10 },
  ];

  private readonly placeholderMostPlayedCategories: MostPlayedCategories[] = [
    { category: Category.REBUS_PUZZLE, total: 12 },
    { category: Category.RIDDLES, total: 8 },
    { category: Category.WORD_PUZZLE, total: 10 },
  ];

  private readonly placeholderTopStudents: StudentWithSubmissions[] = [
    {
      student: {
        id: 'placeholder-1',
        email: 'maria.santos@example.com',
        fname: 'Maria',
        mname: 'L.',
        lname: 'Santos',
        profile: '../../../../assets/images/profile.jpg',
        schoolLevel: SchoolLevel.GRADE_11,
      },
      submissions: [],
      totalMatches: 1,
      points: 120,
    },
    {
      student: {
        id: 'placeholder-2',
        email: 'jose.delacruz@example.com',
        fname: 'Jose',
        mname: 'R.',
        lname: 'Dela Cruz',
        profile: '../../../../assets/images/profile.jpg',
        schoolLevel: SchoolLevel.GRADE_9,
      },
      submissions: [],
      totalMatches: 1,
      points: 140,
    },
    {
      student: {
        id: 'placeholder-3',
        email: 'ariana.ramos@example.com',
        fname: 'Ariana',
        mname: 'M.',
        lname: 'Ramos',
        profile: '../../../../assets/images/profile.jpg',
        schoolLevel: SchoolLevel.GRADE_12,
      },
      submissions: [],
      totalMatches: 1,
      points: 160,
    },
  ];

  games$ = this.quizService.getAllQuiz();
  studentsWithSubmissions$ =
    this.submissionService.getSttudentWithSubmissions();

  submissions$: Observable<Submissions[]> = this.studentsWithSubmissions$.pipe(
    map((studentWithSubmissions) =>
      studentWithSubmissions
        .flatMap((sws) => sws.submissions)
        .sort((a, b) => b.createdAt.getTime() - a.createdAt.getTime())
    )
  );
  averageScorePerGame$ = this.submissions$.pipe(
    map((submissions) => {
      const categories = ['MATH', 'ENGLISH'] as const;

      return categories.map((category) => {
        const filteredSubmissions = submissions.filter(
          (s) => s.quizInfo?.type === category
        );
        const totalMatches = filteredSubmissions.length;
        const totalEarnings = filteredSubmissions.reduce(
          (sum, s) => sum + s.performance.earning,
          0
        );
        const averagePerSubmission =
          totalMatches > 0 ? totalEarnings / totalMatches : 0;

        return { category, averagePerSubmission, totalMatches };
      });
    })
  );

  public barChartData: ChartData<'bar'> = {
    labels: ['MATH', 'ENGLISH'],
    datasets: [
      {
        data: [],
        label: 'Average score per game',
        backgroundColor: ['#FF6384', '#36A2EB'],
      },
    ],
  };
  public barChartOptions: ChartOptions<'bar'> = {
    responsive: true,
  };

  public barChartType: any = 'bar';

  top10Submissions$: Observable<Submissions[]> = this.submissions$.pipe(
    map((submissions) =>
      submissions
        .sort((a, b) => b.createdAt.getTime() - a.createdAt.getTime())
        .slice(0, 8)
    )
  );

  top5Games$ = combineLatest([this.games$, this.submissions$]).pipe(
    map(([games, submissions]) => {
      return games.map((game) => {
        let matchingSubmissions = submissions.filter(
          (submission) => submission.quizInfo?.id === game.id
        );
        return {
          name: game.title,
          submissions: matchingSubmissions.length,
        } as GameWithSubmissions;
      });
    })
  );

  students$: Observable<Students[]> = this.studentsWithSubmissions$.pipe(
    map((studentWithSubmissions) =>
      studentWithSubmissions.map((sws) => sws.student)
    )
  );
  mostPlayedCategories$: Observable<MostPlayedCategories[]> =
    this.submissions$.pipe(
      map((submissions: Submissions[]) => {
        const categoryMap = new Map<Category, number>();

        submissions.forEach((submission) => {
          const category = submission.quizInfo?.category;
          if (category) {
            if (!categoryMap.has(category)) {
              categoryMap.set(category, 0);
            }
            categoryMap.set(category, categoryMap.get(category)! + 1);
          }
        });

        const result: MostPlayedCategories[] = [];
        categoryMap.forEach((total, category) => {
          result.push({ category, total });
        });

        return result;
      })
    );

  public pieChartData: ChartData<'pie'> = {
    labels: [],

    datasets: [
      {
        data: [],
        label: 'Most Played Categories',
        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0'],
      },
    ],
  };

  public pieChartOptions: ChartOptions<'pie'> = {
    responsive: true,
  };

  public pieChartType: any = 'pie';

  recent$: Observable<StudentSubmissionWithGame[]> = combineLatest([
    this.students$,
    this.games$,
    this.top10Submissions$,
  ]).pipe(
    map(([students, games, submissions]) => {
      const recentSubmissions = submissions.map((submission) => {
        const student = students.find((s) => s.id === submission.studentID);
        const game = games.find((g) => g.id === submission.quizInfo?.id);
        return {
          name: displayFullname(
            student?.fname ?? '',
            student?.mname ?? '',
            student?.lname ?? 'unknown user'
          ),
          profile: student?.profile || '',
          gameName: game?.title || 'Unknown',
          levelName: submission.quizInfo?.levels?.name || 'Unknown',
          earning: submission.performance.earning || 0,
        } as StudentSubmissionWithGame;
      });

      return this.applyPlaceholderIfNeeded(
        recentSubmissions,
        this.placeholderRecentSubmissions
      );
    })
  );

  top5Students$: Observable<StudentWithSubmissions[]> =
    this.studentsWithSubmissions$.pipe(
      map((studentWithSubmissions) => {
        const sortedStudents = [...studentWithSubmissions]
          .sort((a, b) => b.points - a.points)
          .slice(0, 5);

        return this.applyPlaceholderIfNeeded(
          sortedStudents,
          this.placeholderTopStudents
        );
      })
    );

  constructor(
    private submissionService: SubmissionsService,
    private quizService: QuizService,
    private cdr: ChangeDetectorRef
  ) {}

  private applyPlaceholderIfNeeded<T>(data: T[], placeholder: T[]): T[] {
    const normalizedData = Array.isArray(data) ? data : [];
    return normalizedData.length < 3 ? placeholder : normalizedData;
  }

  ngOnInit(): void {
    this.averageScorePerGame$.subscribe((categories) => {
      const chartData =
        categories.length < 3 || categories.every((item) => item.totalMatches === 0)
          ? this.placeholderAverageScorePerGame
          : categories;

      this.barChartData.datasets[0].data = chartData.map(
        (category) => category.averagePerSubmission
      );
      this.cdr.detectChanges();
    });

    this.mostPlayedCategories$.subscribe((data) => {
      const chartData =
        data.length < 3 ? this.placeholderMostPlayedCategories : data;

      this.pieChartData.labels = chartData.map((category) => category.category);
      this.pieChartData.datasets[0].data = chartData.map(
        (category) => category.total
      );
      this.cdr.detectChanges();
    });
  }
}
