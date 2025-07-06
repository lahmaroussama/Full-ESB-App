import { NgIf } from '@angular/common';
import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { FormsModule, NgForm, ReactiveFormsModule, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router, RouterLink } from '@angular/router';
import { fuseAnimations } from '@fuse/animations';
import { FuseAlertComponent, FuseAlertType } from '@fuse/components/alert';
import { AuthService } from 'app/core/auth/auth.service';

@Component({
    selector     : 'auth-sign-up',
    templateUrl  : './sign-up.component.html',
    encapsulation: ViewEncapsulation.None,
    animations   : fuseAnimations,
    standalone   : true,
    imports      : [RouterLink, NgIf, FuseAlertComponent, FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, MatCheckboxModule, MatProgressSpinnerModule],
})
export class AuthSignUpComponent implements OnInit
{
    @ViewChild('signUpNgForm') signUpNgForm: NgForm;

    alert: { type: FuseAlertType; message: string } = {
        type   : 'success',
        message: '',
    };
    signUpForm: UntypedFormGroup;
    showAlert: boolean = false;

    /**
     * Constructor
     */
    constructor(
        private _authService: AuthService,
        private _formBuilder: UntypedFormBuilder,
        private _router: Router,
    )
    {
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Lifecycle hooks
    // -----------------------------------------------------------------------------------------------------

    /**
     * On init
     */
    ngOnInit(): void
    {
        // Create the form
        this.signUpForm = this._formBuilder.group({
                username : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
                email    : ['', [Validators.required, Validators.email]],
                password : ['', [Validators.required, Validators.minLength(6), Validators.maxLength(40)]],
            },
        );
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Sign up
     */
    signUp(): void
    {
        // Do nothing if the form is invalid
        if ( this.signUpForm.invalid )
        {
            return;
        }

        // Disable the form
        this.signUpForm.disable();

        // Hide the alert
        this.showAlert = false;

        // Prepare the sign-up data matching backend SignupRequest format
        const signUpData = {
            username: this.signUpForm.get('username').value,
            email: this.signUpForm.get('email').value,
            password: this.signUpForm.get('password').value,
            role: ['user'] // Default role
        };

        // Sign up
        this._authService.signUp(signUpData)
            .subscribe(
                (response) =>
                {
                    // Show success message
                    this.alert = {
                        type: 'success',
                        message: response.message || 'User registered successfully!'
                    };
                    this.showAlert = true;

                    // Navigate to the confirmation required page immediately
                    this._router.navigateByUrl('/confirmation-required');
                },
                (error) =>
                {
                    // Re-enable the form
                    this.signUpForm.enable();

                    // Reset the form
                    this.signUpNgForm.resetForm();

                    // Set the alert with backend error message
                    let errorMessage = 'Something went wrong, please try again.';
                    
                    if (error.error && error.error.message) {
                        errorMessage = error.error.message;
                    } else if (error.status === 400) {
                        errorMessage = 'Invalid data provided. Please check your input.';
                    } else if (error.status === 409) {
                        errorMessage = 'Username or email already exists.';
                    }

                    this.alert = {
                        type: 'error',
                        message: errorMessage
                    };

                    // Show the alert
                    this.showAlert = true;
                },
            );
    }
}