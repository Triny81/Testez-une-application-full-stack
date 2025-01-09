/*

before(() => {
    cy.login(false);
});

describe('List sessions', () => {

    it('should display the correct number of sessions', () => {
        cy.get('.items .item').should('have.length', 3);
    });

    it('should display session details correctly', () => {

        // Vérifier les détails de la première session
        cy.get('.items .item').first().within(() => {
            cy.contains('Yoga for Beginners').should('be.visible');
            cy.contains('December 26, 2024').should('be.visible');
            cy.contains('A session designed for yoga beginners.').should('be.visible');
        });
    });

    it('should display admin actions for admin user', () => {
        cy.login(true); // admin connection

        // Vérifier que les boutons admin existent
        cy.contains('Create').should('be.visible');
        cy.get('.items .item').each((item) => {
            cy.wrap(item).contains('Edit').should('be.visible');
        });
    });
});

describe('Detail of a session for a normal user', () => {
    beforeEach(() => {
        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
                id: 1,
                name: "Yoga For Beginners",
                date: "2024-12-26T00:00:00.000+00:00",
                teacher_id: 1,
                description: "A session designed for yoga beginners.",
                users: [2, 5], // IDs des utilisateurs déjà inscrits
                createdAt: "2024-12-27T14:34:29",
                updatedAt: "2024-12-27T14:35:17"
            }
        }).as('getSession');

        cy.intercept('GET', '/api/teacher/1', {
            statusCode: 200,
            body: {
                id: 1,
                lastName: "DUPONT",
                firstName: "Jean",
                createdAt: "2024-12-27T10:51:20",
                updatedAt: "2024-12-27T10:51:20"
            }
        }).as('getTeacher');

        cy.login(false);
    });

    it('should display session details correctly', () => {
        cy.get('.items .item').first().within(() => {
            cy.contains('Detail').click();
        });

        // Attendre les réponses mockées
        cy.wait('@getSession');
        cy.wait('@getTeacher');

        // Contenu de la session
        cy.contains('Yoga For Beginners').should('be.visible');
        cy.contains('A session designed for yoga beginners.').should('be.visible');
        cy.contains('2 attendees').should('be.visible');
        cy.contains('December 26, 2024').should('be.visible');
        cy.contains('Create at: December 27, 2024').should('be.visible');
        cy.contains('Last update: December 27, 2024').should('be.visible');
        cy.contains('Jean DUPONT').should('be.visible');
    });

    it('should allow a user to participate and unparticipate in a session', () => {
        cy.get('.items .item').first().within(() => {
            cy.contains('Detail').click();
        });

        // PARTICIPATE
        // Simuler l'API pour la participation
        cy.intercept('POST', '/api/session/1/participate/1', {
            statusCode: 200,
            body: {
            }
        }).as('participateSession');

        // Retourner la session mise à jour
        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
                id: 1,
                name: "Yoga For Beginners",
                date: "2024-12-26T00:00:00.000+00:00",
                teacher_id: 1,
                description: "A session designed for yoga beginners.",
                users: [2, 5, 1], // IDs des utilisateurs déjà inscrits
                createdAt: "2024-12-27T14:34:29",
                updatedAt: "2024-12-27T14:35:17"
            }
        }).as('getSessionRefreshParticipate');

        // Ainsi que le professeur
        cy.intercept('GET', '/api/teacher/1', {
            statusCode: 200,
            body: {
                id: 1,
                lastName: "DUPONT",
                firstName: "Jean",
                createdAt: "2024-12-27T10:51:20",
                updatedAt: "2024-12-27T10:51:20"
            }
        }).as('getTeacherRefreshParticipate');

        cy.contains('Participate').click();
        cy.wait('@participateSession');
        cy.wait('@getSessionRefreshParticipate');
        cy.wait('@getTeacherRefreshParticipate');

        cy.contains('Do not participate').should('be.visible');
        cy.contains('3 attendees').should('be.visible');

        // UNPARTICIPATE
        // Simuler l'API pour la désinscription
        cy.intercept('DELETE', '/api/session/1/participate/1', {
            statusCode: 200,
            body: {
            }
        }).as('unparticipateSession');

        // Retourner la session mise à jour
        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
                id: 1,
                name: "Yoga For Beginners",
                date: "2024-12-26T00:00:00.000+00:00",
                teacher_id: 1,
                description: "A session designed for yoga beginners.",
                users: [2, 5], // Retirer l'utilisateur
                createdAt: "2024-12-27T14:34:29",
                updatedAt: "2024-12-27T14:35:17"
            }
        }).as('getSessionRefreshUnparticipate');

        // Ainsi que le professeur
        cy.intercept('GET', '/api/teacher/1', {
            statusCode: 200,
            body: {
                id: 1,
                lastName: "DUPONT",
                firstName: "Jean",
                createdAt: "2024-12-27T10:51:20",
                updatedAt: "2024-12-27T10:51:20"
            }
        }).as('getTeacherRefreshUnparticipate');

        cy.contains('Do not participate').click();
        cy.wait('@unparticipateSession');
        cy.wait('@getSessionRefreshUnparticipate');
        cy.wait('@getTeacherRefreshUnparticipate');

        cy.contains('Participate').should('be.visible');
        cy.contains('2 attendees').should('be.visible');
    });
});

describe('Creation of a session by an admin', () => {
    beforeEach(() => {
        cy.intercept('GET', '/api/teacher', {
            statusCode: 200,
            body: [{
                id: 1,
                lastName: "DUPONT",
                firstName: "Jean",
                createdAt: "2024-12-27T10:51:20",
                updatedAt: "2024-12-27T10:51:20"
            }, {
                id: 2,
                lastName: "DUPOND",
                firstName: "Jeanne",
                createdAt: "2024-12-27T10:51:20",
                updatedAt: "2024-12-27T10:51:20"
            }]
        }).as('getTeacher');

        cy.login(true); // admin connection

        cy.contains('Create').click();
        cy.url().should('include', '/create');
        cy.contains('Create session').should('be.visible');

        cy.wait('@getTeacher');
    });

    it('should create a session', () => {
        const newSession = {
            id: 4,
            name: "Morning Yoga",
            date: "2025-01-01T09:00:00.000+00:00",
            teacher_id: 2,
            description: "A refreshing morning yoga session.",
            users: [],
            createdAt: "2025-01-01T08:00:00",
            updatedAt: "2025-01-01T08:00:00"
        }

        cy.get('input[formControlName="name"]').type('Morning Yoga');
        cy.get('input[formControlName="date"]').type('2025-01-01');

        cy.get('mat-select[formControlName="teacher_id"]').click();
        cy.contains('mat-option', 'Jean DUPONT').click();
        cy.get('mat-select[formControlName="teacher_id"]').should('contain', 'Jean DUPONT');

        cy.get('textarea[formControlName="description"]').type('A refreshing morning yoga session.');

        cy.intercept('POST', '/api/session', {
            statusCode: 201,
            body: newSession
        }).as('createSession');

        cy.intercept('GET', '/api/session', { // Propose une liste de sessions
            statusCode: 200,
            body: [
                {
                    id: 1,
                    name: "Yoga for Beginners",
                    date: "2024-12-26T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "A session designed for yoga beginners.",
                    users: [],
                    createdAt: "2024-12-27T14:34:29",
                    updatedAt: "2024-12-27T14:35:17"
                },
                {
                    id: 2,
                    name: "Advanced Yoga",
                    date: "2024-12-26T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "Challenging yoga poses for experienced practitioners.",
                    users: [],
                    createdAt: "2024-12-27T14:34:29",
                    updatedAt: "2024-12-27T14:35:17"
                },
                {
                    id: 3,
                    name: "Yoga and Meditation",
                    date: "2024-12-26T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "A mix of yoga and meditation techniques.",
                    users: [],
                    createdAt: "2024-12-27T14:34:29",
                    updatedAt: "2024-12-27T14:35:17"
                },
                newSession
            ],
        }).as('getSessions');

        cy.get('button[type="submit"]').click();

        cy.wait('@createSession');
        cy.wait('@getSessions');

        cy.url().should('include', '/sessions');
        cy.contains('Morning Yoga').should('be.visible');
    });

    it('should disable the submit button if a required field is empty', () => {
        cy.get('input[formControlName="name"]').type('Morning Yoga');
        cy.get('input[formControlName="date"]').type('2025-01-01');

        cy.get('mat-select[formControlName="teacher_id"]').click();
        cy.contains('mat-option', 'Jean DUPONT').click();
        cy.get('mat-select[formControlName="teacher_id"]').should('contain', 'Jean DUPONT');

        cy.get('button[type="submit"]').should('be.disabled');
    });
}); 

describe('Delete a session by an admin', () => {
    beforeEach(() => {
        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
                id: 1,
                name: "Yoga For Beginners",
                date: "2024-12-26T00:00:00.000+00:00",
                teacher_id: 1,
                description: "A session designed for yoga beginners.",
                users: [2, 5], // IDs des utilisateurs déjà inscrits
                createdAt: "2024-12-27T14:34:29",
                updatedAt: "2024-12-27T14:35:17"
            }
        }).as('getSession');

        cy.intercept('GET', '/api/teacher/1', {
            statusCode: 200,
            body: {
                id: 1,
                lastName: "DUPONT",
                firstName: "Jean",
                createdAt: "2024-12-27T10:51:20",
                updatedAt: "2024-12-27T10:51:20"
            }
        }).as('getTeacher');

        cy.login(true);
    });

    it('should display session details correctly and deletes it', () => {
        cy.get('.items .item').first().within(() => {
            cy.contains('Detail').click();
        });

        // Attendre les réponses mockées
        cy.wait('@getSession');
        cy.wait('@getTeacher');

        // Contenu de la session
        cy.contains('Yoga For Beginners').should('be.visible');
        cy.contains('A session designed for yoga beginners.').should('be.visible');
        cy.contains('2 attendees').should('be.visible');
        cy.contains('December 26, 2024').should('be.visible');
        cy.contains('Create at: December 27, 2024').should('be.visible');
        cy.contains('Last update: December 27, 2024').should('be.visible');
        cy.contains('Jean DUPONT').should('be.visible');

        // Vérifier que le bouton "Delete" est visible pour un admin
        cy.contains('Delete').should('be.visible');

        // Intercepter la requête DELETE
        cy.intercept('DELETE', '/api/session/1', {
            statusCode: 200,
            body: { message: 'Session deleted successfully' }
        }).as('deleteSession');

        cy.intercept('GET', '/api/session', { // Propose une liste de sessions
            statusCode: 200,
            body: [
                {
                    id: 2,
                    name: "Advanced Yoga",
                    date: "2024-12-26T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "Challenging yoga poses for experienced practitioners.",
                    users: [],
                    createdAt: "2024-12-27T14:34:29",
                    updatedAt: "2024-12-27T14:35:17"
                },
                {
                    id: 3,
                    name: "Yoga and Meditation",
                    date: "2024-12-26T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "A mix of yoga and meditation techniques.",
                    users: [],
                    createdAt: "2024-12-27T14:34:29",
                    updatedAt: "2024-12-27T14:35:17"
                }
            ],
        }).as('getSessions');

        // Cliquer sur le bouton "Delete"
        cy.contains('Delete').click();

        cy.wait('@deleteSession');
        cy.wait('@getSessions');

        // Vérifier que l'utilisateur est redirigé vers la liste des sessions et que la session est bien supprimée
        cy.url().should('include', '/sessions');
        cy.get('.items .item').first().within(() => {
            cy.contains('Yoga for Beginners').should('not.exist');
        });
    });
});
*/
describe('Edit a session by an admin', () => {
    before(() => {
        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
                id: 1,
                name: "Yoga For Beginners",
                date: "2024-12-26T00:00:00.000+00:00",
                teacher_id: 1,
                description: "A session designed for yoga beginners.",
                users: [2, 5], // IDs des utilisateurs déjà inscrits
                createdAt: "2024-12-27T14:34:29",
                updatedAt: "2024-12-27T14:35:17"
            }
        }).as('getSession');

        cy.intercept('GET', '/api/teacher', {
            statusCode: 200,
            body: [{
                id: 1,
                lastName: "DUPONT",
                firstName: "Jean",
                createdAt: "2024-12-27T10:51:20",
                updatedAt: "2024-12-27T10:51:20"
            }, {
                id: 2,
                lastName: "DUPOND",
                firstName: "Jeanne",
                createdAt: "2024-12-27T10:51:20",
                updatedAt: "2024-12-27T10:51:20"
            }]
        }).as('getTeacher');

        cy.login(true);
    });

    it('should display the edit form with pre-filled session data', () => {
        cy.get('.items .item').first().within(() => {
            cy.contains('Edit').click();
        });

        // Attendre les réponses mockées
        cy.wait('@getSession');
        cy.wait('@getTeacher');

        cy.url().should('include', '/sessions/update/1');

        // Vérifier que le formulaire de modification est pré-rempli
        cy.get('input[formControlName="name"]').should('have.value', 'Yoga For Beginners');
        cy.get('input[formControlName="date"]').should('have.value', '2024-12-26');
        cy.get('textarea[formControlName="description"]').should('have.value', 'A session designed for yoga beginners.');
        cy.get('mat-select[formControlName="teacher_id"]').should('contain', 'Jean DUPONT');
    });

    it('should disable the submit button if a required field is empty', () => {
        cy.get('input[formControlName="name"]').clear();
        cy.get('input[formControlName="date"]').clear();
        cy.get('textarea[formControlName="description"]').clear();
        cy.get('button[type="submit"]').should('be.disabled');
    });


    it('should save the edited session', () => {
        const editedSession = {
            id: 1,
            name: "Afternoon Yoga",
            date: "2025-01-02T09:00:00.000+00:00",
            teacher_id: 1,
            description: "A refreshing afternoon yoga session.",
            users: [],
            createdAt: "2025-01-01T08:00:00",
            updatedAt: "2025-01-02T08:00:00"
        };

        // Interceptions
        cy.intercept('PUT', '/api/session/1', (req) => {
            req.reply({
                statusCode: 201,
                body: editedSession
            });
        }).as('editSession');

        cy.intercept('GET', '/api/session*', {
            statusCode: 200,
            body: [
                editedSession,
                {
                    id: 2,
                    name: "Advanced Yoga",
                    date: "2024-12-26T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "Challenging yoga poses for experienced practitioners.",
                    users: [],
                    createdAt: "2024-12-27T14:34:29",
                    updatedAt: "2024-12-27T14:35:17"
                },
                {
                    id: 3,
                    name: "Yoga and Meditation",
                    date: "2024-12-26T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "A mix of yoga and meditation techniques.",
                    users: [],
                    createdAt: "2024-12-27T14:34:29",
                    updatedAt: "2024-12-27T14:35:17"
                }
            ]
        }).as('getSessions');

        // Modifier les champs
        cy.get('input[formControlName="name"]').clear().type('Afternoon Yoga');
        cy.get('input[formControlName="date"]').clear().type('2025-01-02');
        cy.get('mat-select[formControlName="teacher_id"]').click();
        cy.contains('mat-option', 'Jeanne DUPOND').click();
        cy.get('textarea[formControlName="description"]').clear().type('A refreshing afternoon yoga session.');

        // Soumettre le formulaire
        cy.get('button[type="submit"]').click();

        // Attendre les interceptions
        cy.wait('@editSession');
        cy.wait('@getSessions');

        // Vérifier que la session modifiée apparaît dans la liste
        cy.contains('Afternoon Yoga').should('be.visible');
    });
});
