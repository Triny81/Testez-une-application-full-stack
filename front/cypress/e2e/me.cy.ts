describe('User Information Page', () => {
    before(() => {
        cy.login(false);
    });

    it('should display user information correctly', () => {
        cy.intercept('GET', '/api/user/1', {
            statusCode: 200,
            body: {
                id: 1,
                username: 'MGarnier',
                firstName: 'Garnier',
                lastName: 'Michel',
                email: 'michel.garnier@test.com',
                admin: false,
                createdAt: "2024-12-27T10:51:20",
                updatedAt: "2024-12-27T10:51:20"
            }
        }).as('getUser');

        cy.contains('Account').should('be.visible');
        cy.contains('Account').click();

        cy.contains('Name: Garnier MICHEL').should('be.visible');
        cy.contains('Email: michel.garnier@test.com').should('be.visible');
        cy.contains('Create at: December 27, 2024').should('be.visible');
        cy.contains('Last update: December 27, 2024').should('be.visible');
    });

    it('should allow non-admin users to delete their account', () => {
        cy.contains('Delete my account:').should('be.visible');
        cy.get('button').contains('Detail').should('be.visible'); // PAS BON LE NOM

        cy.intercept('DELETE', '/api/user/1', {
            statusCode: 200,
            body: { message: 'Account deleted successfully' }
        }).as('deleteAccount');

        // Cliquer sur le bouton "Delete" qui d'aailleurs n'est pas bien nommé
        cy.contains('Detail').click();

        // Attendre la confirmation de suppression
        cy.wait('@deleteAccount');

        // Vérifier que l'état de connexion n'est plus actif
        cy.contains('Login').should('be.visible');
        cy.contains('Register').should('be.visible');
        cy.contains('Logout').should('not.exist');
    });
});

describe('Admin Information Page', () => {
    before(() => {
        cy.login(true);
    });

    it('should display user information correctly', () => {
        cy.intercept('GET', '/api/user/1', {
            statusCode: 200,
            body: {
                id: 1,
                username: 'MGarnier',
                firstName: 'Garnier',
                lastName: 'Michel',
                email: 'michel.garnier@test.com',
                admin: true,
                createdAt: "2024-12-27T10:51:20",
                updatedAt: "2024-12-27T10:51:20"
            }
        }).as('getUser');

        cy.contains('Account').should('be.visible');
        cy.contains('Account').click();

        cy.contains('Name: Garnier MICHEL').should('be.visible');
        cy.contains('Email: michel.garnier@test.com').should('be.visible');
        cy.contains('Create at: December 27, 2024').should('be.visible');
        cy.contains('Last update: December 27, 2024').should('be.visible');

        cy.contains('Delete my account:').should('not.exist');
        cy.get('button').contains('Delete').should('not.exist');
    });

    it('should navigate back when back button is clicked', () => {
        cy.get('button[mat-icon-button]').click();

        cy.url().should('not.include', '/me');
    });
});