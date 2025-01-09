declare namespace Cypress {
    interface Chainable {
        login(isAdmin: boolean): void;
    }
}

Cypress.Commands.add("login", (isAdmin: boolean) => { // auto connexion
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
        body: {
            id: 1,
            username: 'MGarnier',
            firstName: 'Garnier',
            lastName: 'Michel',
            email: 'michel.garnier@test.com',
            admin: isAdmin,
            createdAt: "2024-12-27T10:51:20",
            updatedAt: "2024-12-27T10:51:20"
        },
    });

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
            }
        ],
    }).as('getSessions');

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/sessions');
});
