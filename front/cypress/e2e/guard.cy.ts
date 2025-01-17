describe('AuthGuard', () => {
    it('should redirect to /login if the user is not logged in', () => {
        cy.visit('/sessions', {
        });

        // Vérifier que l'utilisateur est redirigé vers /404
        cy.url().should('include', '/login');
        cy.contains('Login').should('be.visible');
    });

    it('should allow access if the user is logged in', () => {
        cy.login(false);

        // Vérifier que l'utilisateur peut accéder à la route protégée
        cy.url().should('include', '/sessions');
    });
});