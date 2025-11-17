// Authentication and permission utilities

export const authService = {
  // Get current user's role
  getRole() {
    return localStorage.getItem('role') || 'USER';
  },

  // Check if user is admin
  isAdmin() {
    // Admin user is explicitly the username 'axzil'
    return this.getUsername().toLowerCase() === 'axzil';
  },

  // Get current username
  getUsername() {
    return localStorage.getItem('username') || '';
  },

  // Check if user is logged in
  isLoggedIn() {
    return !!localStorage.getItem('token');
  },

  // Logout user
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
  }
};

export default authService;
