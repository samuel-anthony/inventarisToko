<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateCartCustomersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('cart_customers', function (Blueprint $table) {
            $table->bigIncrements('cart_id');
            $table->unsignedBigInteger('makanan_id');
            $table->unsignedBigInteger('user_id');
            $table->foreign('makanan_id')->references('makanan_id')->on('makanans');
            $table->foreign('user_id')->references('id')->on('users');
            $table->integer('harga_makanan');
            $table->integer('jumlah')->default(1);
            $table->string('notes')->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('cart_customers');
    }
}
