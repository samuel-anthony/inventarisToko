<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreatePesananDetailsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('pesanan_details', function (Blueprint $table) {
            $table->bigIncrements('pesanan_detail_id');
            $table->unsignedBigInteger('makanan_id');
            $table->foreign('makanan_id')->references('makanan_id')->on('makanans');
            $table->unsignedBigInteger('pesanan_master_id');
            $table->foreign('pesanan_master_id')->references('pesanan_master_id')->on('pesanan_masters');
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
        Schema::dropIfExists('pesanan_details');
    }
}
